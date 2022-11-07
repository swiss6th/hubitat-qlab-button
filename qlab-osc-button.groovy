/*

QLab OSC Button

This driver sends a text string via UDP to a device on the network. Strings are set in the device preferences and can be sent by triggering a push, doubleTap, or hold command. The original intent was to send plaintext OSC commands to Figure 53's QLab, but it could be used for any network device that is looking for commands via plaintext UDP.

Set up a Button Controller or Rule Machine rule to trigger button presses on this driver. Then, any button compatible with Hubitat can send commands.

*/

metadata {
	definition(name: "QLab OSC Button", namespace: "swiss6th", author: "Andrew Hall") {
		capability "Actuator"
		capability "PushableButton"
		capability "DoubleTapableButton"
		capability "HoldableButton"

		command "push", ["NUMBER"]
		command "doubleTap", ["NUMBER"]
		command "hold", ["NUMBER"]

		attribute "numberOfButtons", "number"
	}
	preferences {
		section("Target Device") {
			input "qlabAddress", "text", title: "<strong>Address of QLab</strong>:", required: true, multiple: false, displayDuringSetup: true
			input "qlabPort", "number", title: "<strong>Port of QLab</strong>:", required: true, multiple: false, displayDuringSetup: true, range: "0..65353", defaultValue: 53535
		}
		section("Commands") {
			input "pushedCommandString", "text", title: "<strong>Command string to send on pushed</strong>:", required: true, multiple: false, displayDuringSetup: true, defaultValue: "/go"
			input "doubleTappedCommandString", "text", title: "<strong>Command string to send on doubleTapped</strong>:", required: true, multiple: false, displayDuringSetup: true, defaultValue: "/go"
			input "heldCommandString", "text", title: "<strong>Command string to send on held</strong>:", required: true, multiple: false, displayDuringSetup: true, defaultValue: "/go"
		}
	}
}

def updated() {
	state.clear()
	sendEvent(name: "numberOfButtons", value: 1)
	log.info("Preferences saved")
}

def installed() {
	log.info("Installed")
	updated()
}

def sendUdpPacket(String message) {
	if ((message == "") || (qlabAddress == "") || (qlabPort == null)) {
		log.info("Command string or destination not set")
		return
	}
	String destination = "${qlabAddress}:${qlabPort}"
	log.info("Sending ${message} to ${destination}")
	def udpToSend = new hubitat.device.HubAction(
		message,
		hubitat.device.Protocol.LAN,
		[
			type: hubitat.device.HubAction.Type.LAN_TYPE_UDPCLIENT, 
			destinationAddress: destination,
			ignoreResponse: true
		]
	)
	sendHubCommand(udpToSend)
}

def sendButtonEvent(action) {
	String logMessage = "${device.displayName} button 1 was ${action}"
	log.info(logMessage)
	sendEvent(name: action, value: 1, descriptionText: logMessage, isStateChange: true, type: "digital")
}

def push(button) {
	sendButtonEvent("pushed")
	sendUdpPacket(pushedCommandString)
}

def doubleTap(button) {
	sendButtonEvent("doubleTapped")
	sendUdpPacket(doubleTappedCommandString)
}

def hold(button) {
	sendButtonEvent("held")
	sendUdpPacket(heldCommandString)
}
