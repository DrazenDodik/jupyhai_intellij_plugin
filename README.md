# Jupyhai with IntelliJ

## Install Jupyhai to your machine

(if needed, you would have to do this inside the virtualenv)

Generate a whl for Jupyhai

1. Run `pip install ./jupyhai-1.0.1-py2.py3-none-any.whl` to install Jupyhai on your machine
1. Install and enable the extension to your notebooks with 
    * `jupyter nbextension install --symlink --py jupyhai`
    * `jupyter nbextension enable jupyhai --py`

## Install the IntelliJ plugin

1. Open IntelliJ->Preferences/Settings->Plugins and click on the gear icon to install a plugin from disk.
1. Select the zip file and if needed restart your IDE.

## Using the plugin

1. If you haven't yet, login to Valohai in your terminal with `vh login`
1. Launch a new notebook with `jupyter notebook`
1. Open IntelliJ->Preferences/Settings->Tools->Valohai Configuration to setup your notebook server address and token.

> :exclamation: You'll need to update the server token every time you start a new server or the old server tokens.

Under those settings you can also set project specific settings:

* **Valohai Project ID:** Go to app.valohai.com and open your project. Go to Settings->General and copy the ID from the bottom of the page.
* **Docker image:** By default this should be valohai/pypermill but you might use some custom images, if they're working.
* **Ignore files:** Any files that Valohai should ignore when uploading notebook code to Valohai. By default it will upload the whole folder (except .ipynb notebooks)
