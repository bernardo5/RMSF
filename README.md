# RMSF - Redes Móveis e Sem Fios (Wireless Mobile Networks)

The purpose of the project was to create an Android App in order to be able to track the temperature read by a sensor.

The sensor is based on an arduino device (Akeru) connected to the SigFox network. The sensor sends the temperature values periodically to the network.

On the client side, the user logs in on the app, which verifies the credentials with the database help. In case of success in retrieves periodically the temperature information from the SigFox API.

The user can define temperature thresholds to receive alarms on the mobile phone.
