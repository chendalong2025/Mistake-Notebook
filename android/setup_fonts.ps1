param()

$FONT_DIR = "assets\fonts"
$FONT_FILE = "$FONT_DIR\NotoSansSC.ttf"

if (-not (Test-Path $FONT_DIR)) {
    New-Item -ItemType Directory -Force -Path $FONT_DIR | Out-Null
    Write-Host "created: $FONT_DIR"
}

if (Test-Path $FONT_FILE) {
    Write-Host "font already exists: $FONT_FILE"
}
else {
    Write-Host "downloading Noto Sans SC font..."
    $url = "https://fonts.gstatic.com/s/notosanssc/v26/k3kCo84MPvpLmixcA63oeALhLOCT-7hQkHXMfA.ttf"
    try {
        Invoke-WebRequest -Uri $url -OutFile $FONT_FILE -UseBasicParsing
        Write-Host "OK: $FONT_FILE"
    }
    catch {
        Write-Host "FAIL: download error, place NotoSansSC.ttf manually into assets\fonts\"
    }
}

Write-Host "Done. Run: uv run python main.py"
