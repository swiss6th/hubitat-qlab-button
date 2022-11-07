# QLab OSC Button

This [Hubitat Elevation](https://hubitat.com) driver sends a text string via UDP to a device on the network. Strings are set in the device preferences and can be sent by triggering a `push`, `doubleTap`, or `hold` command. The original intent was to send [plaintext OSC commands](https://qlab.app/docs/v5/scripting/osc-dictionary-v5#getting-started) to Figure 53's [QLab](https://qlab.app), but it could be used for any network device that is looking for commands via plaintext UDP.

Set up a [Button Controller](https://docs2.hubitat.com/apps/button-controllers/button-controller-5-1) or [Rule Machine](https://docs2.hubitat.com/apps/rule-machine) rule to trigger button presses on this driver. Then, any button compatible with Hubitat (e.g., [Sonoff SNZB-01](https://sonoff.tech/product/gateway-amd-sensors/snzb-01/)) can send commands.

This is a purpose-built driver for my own workflow, but I will entertain pull requests. Feel free to fork.
