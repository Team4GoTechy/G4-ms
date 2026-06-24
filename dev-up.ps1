#Requires -Version 5.1
$ErrorActionPreference = 'Stop'

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location -LiteralPath $ScriptDir

$LoadedEnvVars = @{}

function Write-Step {
    param([string]$Message)
    Write-Host "==> " -ForegroundColor Cyan -NoNewline
    Write-Host $Message
}

function Write-Ok {
    param([string]$Message)
    Write-Host "  [OK] " -ForegroundColor Green -NoNewline
    Write-Host $Message
}

function Write-Warn {
    param([string]$Message)
    Write-Host "  [!] " -ForegroundColor Yellow -NoNewline
    Write-Host $Message
}

function Write-Err {
    param([string]$Message)
    Write-Host "  [X] " -ForegroundColor Red -NoNewline
    Write-Host $Message
}

function Test-Command {
    param([string]$Name)
    return [bool](Get-Command -Name $Name -ErrorAction SilentlyContinue)
}

function Load-DotEnv {
    param([string]$Path)
    if (-not (Test-Path -LiteralPath $Path)) {
        throw "No se encontro el archivo .env en: $Path"
    }
    $loaded = 0
    Get-Content -LiteralPath $Path | ForEach-Object {
        $line = $_.Trim()
        if ([string]::IsNullOrWhiteSpace($line)) { return }
        if ($line.StartsWith('#')) { return }
        if ($line -match '^\s*([^=#]+)=(.*)$') {
            $name = $Matches[1].Trim()
            $value = $Matches[2].Trim()
            if ($value.Length -ge 2) {
                $first = $value[0]
                $last = $value[$value.Length - 1]
                if (($first -eq '"' -and $last -eq '"') -or ($first -eq "'" -and $last -eq "'")) {
                    $value = $value.Substring(1, $value.Length - 2)
                }
            }
            [System.Environment]::SetEnvironmentVariable($name, $value, 'Process')
            $script:LoadedEnvVars[$name] = $true
            $loaded++
        }
    }
    return $loaded
}

function Wait-ForPostgres {
    param(
        [string]$Host,
        [int]$Port,
        [string]$User,
        [string]$Password,
        [string]$Database,
        [int]$TimeoutSeconds = 60
    )
    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    $attempt = 0
    while ((Get-Date) -lt $deadline) {
        $attempt++
        try {
            $env:PGPASSWORD = $Password
            $result = & psql -h $Host -p $Port -U $User -d $Database -c "SELECT 1" -t -A 2>$null
            Remove-Item Env:\PGPASSWORD -ErrorAction SilentlyContinue
            if ($LASTEXITCODE -eq 0 -and $result -eq '1') {
                return $true
            }
        } catch {
        }
        Write-Host "." -NoNewline
        Start-Sleep -Seconds 2
    }
    Write-Host ""
    return $false
}

try {
    Write-Step "Cargando variables desde .env"
    $count = Load-DotEnv -Path (Join-Path $ScriptDir '.env')
    Write-Ok "$count variable(s) cargada(s) al proceso"
    foreach ($key in 'DB_HOST','DB_PORT','DB_NAME','DB_USER','DB_PASSWORD','JWT_SECRET','CLOUDINARY_CLOUD_NAME') {
        if (-not $LoadedEnvVars.ContainsKey($key)) {
            Write-Warn "Variable $key no esta en .env (se usara el default del application.yml)"
        }
    }

    Write-Step "Verificando Docker"
    if (-not (Test-Command 'docker')) {
        throw "Docker no esta instalado o no esta en el PATH. Instala Docker Desktop."
    }
    docker info *> $null
    if ($LASTEXITCODE -ne 0) {
        throw "Docker no esta corriendo. Abre Docker Desktop y reintenta."
    }
    Write-Ok "Docker esta corriendo"

    Write-Step "Levantando base de datos (docker compose up -d)"
    $composeFile = Join-Path $ScriptDir 'docker-compose.yml'
    if (-not (Test-Path -LiteralPath $composeFile)) {
        throw "No se encontro docker-compose.yml en: $composeFile"
    }
    docker compose -f $composeFile up -d
    if ($LASTEXITCODE -ne 0) {
        throw "Fallo 'docker compose up -d'"
    }

    Write-Step "Esperando a que Postgres acepte conexiones"
    $dbHost = [System.Environment]::GetEnvironmentVariable('DB_HOST', 'Process')
    if ([string]::IsNullOrWhiteSpace($dbHost)) { $dbHost = 'localhost' }
    $dbPort = 0
    $portStr = [System.Environment]::GetEnvironmentVariable('DB_PORT', 'Process')
    if (-not [int]::TryParse($portStr, [ref]$dbPort)) { $dbPort = 5432 }
    $dbName = [System.Environment]::GetEnvironmentVariable('DB_NAME', 'Process')
    if ([string]::IsNullOrWhiteSpace($dbName)) { $dbName = 'petshop_ecommerce' }
    $dbUser = [System.Environment]::GetEnvironmentVariable('DB_USER', 'Process')
    if ([string]::IsNullOrWhiteSpace($dbUser)) { $dbUser = 'petshop_admin' }
    $dbPass = [System.Environment]::GetEnvironmentVariable('DB_PASSWORD', 'Process')
    if ([string]::IsNullOrWhiteSpace($dbPass)) {
        throw "DB_PASSWORD no esta definida y es obligatoria para SCRAM auth. Seteala en .env"
    }

    if (Test-Command 'psql') {
        if (Wait-ForPostgres -Host $dbHost -Port $dbPort -User $dbUser -Password $dbPass -Database $dbName) {
            Write-Ok "Postgres responde en ${dbHost}:${dbPort}/${dbName}"
        } else {
            throw "Postgres no respondio despues de 60s. Revisa 'docker compose logs db'."
        }
    } else {
        Write-Warn "psql no esta instalado, salto el ping. Esperando 10s..."
        Start-Sleep -Seconds 10
    }

    Write-Step "Limpiando build previo y arrancando backend (mvnw clean spring-boot:run)"
    Write-Host "     Ctrl+C para detener el servidor" -ForegroundColor DarkGray
    Write-Host ""
    & .\mvnw.cmd clean spring-boot:run
    exit $LASTEXITCODE
}
catch {
    Write-Host ""
    Write-Err $_.Exception.Message
    exit 1
}
