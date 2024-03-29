import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import java.util.Random;
public class Client {
    public int port;
    public String address = null ;
    public ObjectInputStream objectInput = null;
    public ObjectOutputStream objectOutput = null;

    public Socket clientSideSocket = null;


    public Client(String address, int port){
        this.address = address;
        this.port = port;
        Ball b = new Ball();
        try{

            clientSideSocket = new Socket(address, port);

            objectOutput = new ObjectOutputStream(clientSideSocket.getOutputStream());
            objectInput = new ObjectInputStream(new BufferedInputStream(clientSideSocket.getInputStream()));

//            Start toss
            b.tossValue = generateRandomNumber();
            objectOutput.writeObject(b);
            while(true){
                b =  (Ball) objectInput.readObject();
                if(b.tossResult.equals("Tie")){
                    b.tossValue = generateRandomNumber();
                    objectOutput.writeObject(b);
                }
                else {
                    System.out.println(b.tossResult +" won the toss");
                    break;
                }

            }

            if(b.tossResult.equals("Client")){
                b.message = "Ping";
                objectOutput.writeObject(b);
            }
            else {
                b.message = null;
                objectOutput.writeObject(b);
            }

            while (true){
                if(b.tossResult.equals("Client")){
                    b = (Ball) objectInput.readObject();
                    if(b.message != null) System.out.println("CLIENT received ->" + b.message);
                    b.message = "Ping";
                    objectOutput.writeObject(b);
                }
                else{
                    b = (Ball) objectInput.readObject();
                    if(b.message != null) System.out.println("CLIENT received ->" + b.message);
                    b.message = "Pong";
                    objectOutput.writeObject(b);
                }
                TimeUnit.SECONDS.sleep(2);
            }




        }
        catch(Exception i){
            System.out.println(i);
        }


    }
    public int generateRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(2);
    }


    public static void main(String[] args){
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Client client = new Client(host, port);
    }
}
