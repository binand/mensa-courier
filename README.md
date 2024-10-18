# SMS Forwarder and Receiver

This app forwards all SMS messages received on an Android device to another device. It uses Firebase Messaging for transport.

## Usage

1. Install the Forwarder app in the device that receives SMS messages. **For its core function, it needs the SMS permission. Allow it.**
2. Install the Receiver app in the device on which you wish to see these SMS messages. **For its core function, it needs the Contacts permission. Allow it.**
3. In the Receiver app, copy the Firebase token from the overflow menu. Take this over to the Forwarder app.
4. If the Forwarder app is showing an existing token, clear it and then paste the new token from clipboard.
5. That's it. All SMS messages on the Forwarder device will be now forwarded to the Receiver app.

## Points to Note

Remember that there is quite a high potential for misuse of this app. Please be aware of the legal, security and privacy implications of using an app like this.

## Future Enhancements

1. There are no filtering options. We could try filtering on Sender or Contents (or even time of day). One good use-case is to forward only OTP messages.
2. Aggregate metrics. Number of messages processed, time taken etc.

## License

This app is released under the following license.

### The Beerware License

As long as you retain this notice you can do whatever you want with this app. If we meet some day, and you think this app is worth it, you can buy me a beer in return.
