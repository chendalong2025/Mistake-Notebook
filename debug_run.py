import sys, traceback
sys.path.insert(0, 'src')
try:
    from mistake_notebook.ui.app import MainApp, ICON_ICO, ICON_PNG
    print(f"ICO 路径: {ICON_ICO} 存在={ICON_ICO.exists()}")
    print(f"PNG 路径: {ICON_PNG} 存在={ICON_PNG.exists()}")
    app = MainApp()
    app.after(400, app.destroy)
    app.mainloop()
    print('OK')
except Exception as e:
    traceback.print_exc()
    sys.exit(1)
