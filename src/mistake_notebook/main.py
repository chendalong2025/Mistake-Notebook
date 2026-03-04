"""程序入口"""

from mistake_notebook.ui.app import MainApp


def main() -> None:
    app = MainApp()
    app.mainloop()


if __name__ == "__main__":
    main()
