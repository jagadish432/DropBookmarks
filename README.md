# HTTPS

## Command to generate a keypair using JAVA sdk

```aidl
keytool -genkeypair -keyalg RSA -dname "CN=localhost" -keystore dropbookmarks.keystore -keypass <key password> -storepass <store password>
```

* we need to provide/set the key password and store password.

