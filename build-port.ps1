param(
    [ValidateSet('all','26.1.2','26.2')][string]$Target = 'all'
)
$ErrorActionPreference = 'Stop'
$portTargets = if ($Target -eq 'all') { @('26.1.2','26.2') } else { @($Target) }
Push-Location $PSScriptRoot
try {
    foreach ($portTarget in $portTargets) {
        # Each Minecraft target gets its own project cache so the two NeoForge setups never collide.
        $portArgs = @('build', "-PmcTarget=$portTarget", '--project-cache-dir', "$PSScriptRoot/.gradle/$portTarget", '--console=plain')
        & "$PSScriptRoot/gradlew.bat" @portArgs
        if ($LASTEXITCODE -ne 0) { throw "Build failed for $portTarget" }
    }
} finally { Pop-Location }
