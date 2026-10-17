$ErrorActionPreference = 'Stop'

$maven = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $maven) {
    throw 'Maven was not found. Install Maven and add it to PATH before starting the backend.'
}

$previousPassword = [Environment]::GetEnvironmentVariable('DB_PASSWORD', 'Process')
$securePassword = Read-Host 'Enter your MySQL password' -AsSecureString
$passwordPointer = [IntPtr]::Zero

try {
    $passwordPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    $env:DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPointer)

    & $maven.Source -f (Join-Path $PSScriptRoot 'pom.xml') spring-boot:run
    $mavenExitCode = $LASTEXITCODE
}
finally {
    if ($passwordPointer -ne [IntPtr]::Zero) {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPointer)
    }

    $securePassword.Dispose()

    if ($null -eq $previousPassword) {
        Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
    }
    else {
        $env:DB_PASSWORD = $previousPassword
    }
}

if ($mavenExitCode -ne 0) {
    exit $mavenExitCode
}
