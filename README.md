# Pack-MC
A Minecraft plugin that hosts a resource pack upload server on the server that's hosting the Minecraft server. 

# How to install this plugin on your paper server.

## Step 1:
Go to the releases tab on the right side of the repository and download the latest release.

## Step 2:
Drop that JAR file into your "plugins" folder for your paper server.

## Step 3:
Start your paper server up and make sure you see an enabled message from PackMC.

Everything should be working properly and all you have to do is go to ``localhost:3000`` or ``yourserversip:3000``.


# TODO:
- ~~Add an upload webpage to easily upload resource packs~~
- ~~Make checks to ensure resource packs are resource packs~~
- Optimize web server to ensure it doesn't eat all of the memory
- Add a computer resource cap so it doesn't get overloaded with requests
- Toggleable options in config such as force resource packs
- Create an API along with this for easy integretion into peoples plugin(s)
