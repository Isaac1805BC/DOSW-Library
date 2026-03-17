$path = 'C:\USERS\ISAAC\ONEDRIVE\DESKTOP\DOSW\ACTIVIDAD EN CLASE\DOSW-LIBRARY\SRC\MAIN\JAVA\edu\eci\dosw\tdd'

New-Item -ItemType Directory -Force -Path "$path\core" | Out-Null
foreach ($dir in @('model','service','exception','util')) {
    if (Test-Path "$path\$dir") {
        Move-Item -Path "$path\$dir" -Destination "$path\core\$dir" -Force
    }
}

Get-ChildItem -Path $path -Recurse -Filter *.java | ForEach-Object {
    $content = [IO.File]::ReadAllText($_.FullName)
    if ($content) {
        $newContent = $content -replace 'edu\.eci\.dosw\.tdd\.(model|service|exception|util)', 'edu.eci.dosw.tdd.core.$1'
        if ($content -ne $newContent) {
            [IO.File]::WriteAllText($_.FullName, $newContent)
        }
    }
}
