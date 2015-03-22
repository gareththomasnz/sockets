public class ServerConstants {
	public static final int CHAT_MESSAGE = 0;
	public static final int EXIT_MESSAGE = 1;
	public static final int CHAT_BROADCAST = 2;
	public static final int PRIVATE_MESSAGE = 3; //TODO develop your own protocol to handle private messages
	public static final int REGISTER_CLIENT = 4; // TODO develop your own protocol to handle the registration of a new client. This message will need to send a client's nickname to the centralised server.
	public static final int REGISTER_BROADCAST = 5; // TODO develop your own protocol to handle the broadcast of a new client, to all other clients connected to the server
}
