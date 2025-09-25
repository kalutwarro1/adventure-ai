Instalación de generador de imagenes IA:

1-Install Python 3.10.6
2-Download Python 3.10.6
3-Run installer → check ✅ Add Python to PATH → choose Customize installation → make sure pip + venv are selected.
4-python --version -> Debería mostrar Python 3.10.6


5-
1-.\webui-user.bat 
2- Debemos ver que se ejecute el cmd automáticamente:
set COMMANDLINE_ARGS=--api --skip-torch-cuda-test --use-cpu all
3- El proceso se va a levantar en: http://127.0.0.1:7860/