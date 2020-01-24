# tak
Hyper secret project from Evil Corp

### Enable USB permissions
You must enable read/write permission on USB devices. Each operating system has it's own facilities.
##### Linux
Crete a rule file for UDEV system and restart it.
```bash
$ sudo sh -c 'echo "SUBSYSTEM==\"usb\", ATTRS{idVendor}==\"1ffb\", MODE=\"0666\"" > /etc/udev/rules.d/50-pololu.rules && /etc/init.d/udev restart'
```
##### Mac
```bash
$ sudo sh -c 'echo "SUBSYSTEM==\"usb\", ATTRS{idVendor}==\"1ffb\", MODE=\"0666\"" > /etc/udev/rules.d/50-pololu.rules && /etc/init.d/udev restart'
```
##### Windows
```bash
$ sudo sh -c 'echo "SUBSYSTEM==\"usb\", ATTRS{idVendor}==\"1ffb\", MODE=\"0666\"" > /etc/udev/rules.d/50-pololu.rules && /etc/init.d/udev restart'
```