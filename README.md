# SMS To Email

This app forwards all SMS messages received on the Android device to a specified email address.

## Usage

1. Install the app.
2. Launch it.
3. **For its core function, it needs the SMS permission. Allow it.**
4. Most people don't need to change the server. Leave it alone.
5. Enter the email address and password.
6. That's it. All SMS messages will be now send to this email address.

## Points to Note

### Google-Specific Instructions

1. First and foremost, do not use your primary Google account with this app. I suggest creating a throwaway account just for this purpose.
2. If you have 2FA enabled for your Google account, then you need to have an App Password. Please follow [Google's instructions on how to do this](https://support.google.com/accounts/answer/185833).
3. If you do not have 2FA enabled for your Google account, then you need to enable Less Secure App Access. [Google's instructions for this](https://support.google.com/accounts/answer/6010255).
4. The SMS messages in the Gmail Inbox can be searched for by: `from:me to:me subject:(SMS From)`. You might want to create a label and a filter.

### Other Important Points

Pretty much everything in the app is hardcoded.

1. The app internally uses port 465. If you change the server please ensure this port is supported.
2. The format of the message is hardcoded too.

Also remember that there is quite a high potential for misuse of this app. Please be aware of the legal, security and privacy implications of using an app like this.

The app stores the Google account's password in SharedPreferences without any protection. Therefore do not use a Google account you care about, in this app.

### Future Enhancements

1. If data connection is not available the message is not forwarded. We need a retry system.
2. There are no filtering options. We could try filtering on Sender or Contents (or even time of day). One good use-case is to forward only OTP messages.
3. Right now the Broadcast Receiver itself does the SMS sending. This is a problem. We need to decouple these and switch to asynchronous sending.
4. Other modes of delivery - WhatsApp? Telegram?
5. Aggregate metrics. Number of messages processed, time taken etc.

## License

This app is released under the following license.

### The Beerware License

As long as you retain this notice you can do whatever you want with this app. If we meet some day, and you think this app is worth it, you can buy me a beer in return.
