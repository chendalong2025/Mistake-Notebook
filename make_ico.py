"""将 PNG 转换为多尺寸 ICO 文件"""
from PIL import Image
from pathlib import Path

src = Path("Mistake-Notebook.png")
dst = Path("assets/icon.ico")
dst.parent.mkdir(exist_ok=True)

img = Image.open(src).convert("RGBA")
sizes = [16, 24, 32, 48, 64, 128, 256]
img.save(dst, format="ICO", sizes=[(s, s) for s in sizes])
print(f"ICO 已生成: {dst}")
