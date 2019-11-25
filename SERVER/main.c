//
// Created by irina on 16.10.19.
//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>
#include <sys/queue.h>
//#include "player.h"
#include "game.h"

//#define PORT 10126
//#define LOBBYSIZE 3  //should be changable from an input file

place lobby[LOBBYSIZE]; //declare lobby

void initializeLobby(){
    lobby[0].name = "Mistnost 1";
    lobby[0].id = 0;
    lobby[0].max_pocet_hracu = 2;
    lobby[0].pocet_hracu = 0;
    lobby[1].name = "Mistnost 2";
    lobby[1].id = 1;
    lobby[1].max_pocet_hracu = 2;
    lobby[1].pocet_hracu = 0;
    lobby[2].name = "Mistnost 3";
    lobby[2].id = 2;
    lobby[2].max_pocet_hracu = 2;
    lobby[2].pocet_hracu = 0;
//
}

void sendMess(int newSocket, char *sbuff, int lenBuff) {
    printf("Ve funkci sendMess1\n");
    write(newSocket, sbuff, lenBuff);
    printf("Ve funkci sendMess2\n");
    //bzero(*sbuff,lenBuff);
    //sbuff = "";
    printf("Ve funkci sendMess3\n");
}

typedef char *str[3]; // char used in addToLobby for converting int -> string

//void clearMistnost(place *m){
//    m->pocet_hracu = 0;
//    m->player1.gameScore = 0;
//    m->player2.gameScore = 0;
//}

//recieve message from clients
void receive(int socket, char *buff){
    int size = 256;
    char *user;
    char *command;
    int reader = recv(socket, buff, size * sizeof(char), 0);
    if (reader == -1) {
        printf("BREAK reader == -1\n");
        perror("recv()");
        exit(0);
    } else if (reader == 0) {
        printf("BREAK reader == 0\n");
        exit(0);
    }
//    else {
//        command = strtok(buff, ":"); //split received message in name and command
//        user = command;
//        command = strtok(NULL, ":");
//        printf("\n%s sent: %s\n", user, command);
//     }
}

char parseRecvMess(char *buff){
    char a[2];
    char *user;
    char *command;
    command = strtok(buff, ":"); //split received message in name and command
    user = command;
    command = strtok(NULL, ":");
    a[0] = (char) user;
    a[1] = (char) command;
    return (char) a;
}

//send a message to all players
void broadcast(int socket, int socket1, char *sbuff) {
    //printf("Broadcast function called\n");
    send(socket, sbuff, 256, 0);
    send(socket1, sbuff, 256, 0);
}


//Add player to lobby
void addToLobby(player *hrac){
    int size = 256;
    char sbuff[size];
    char buff[size];
    memset(sbuff, '\0', size);
    strcpy(sbuff, "Ahoj " );
    strcat(sbuff, hrac->name);
    strcat(sbuff, " vitej ve hre. Vyber si mistnost:+");
    for( int i = 0; i< LOBBYSIZE; i++) {
        strcat(sbuff, lobby[i].name);
        strcat(sbuff, ": pocet hracu = ");
        str str;// TODO
        //itoa(lobby[i].pocet_hracu, str, 10);
        sprintf(str, "%d", lobby[i].pocet_hracu);
        strcat(sbuff, str);
        strcat(sbuff, "+");
    }
    strcat(sbuff, ":\n");
    send(hrac->socket, sbuff, sizeof(sbuff), 0);
    bzero(sbuff, size);
    int volba = 1000;

    while ((volba < 1) || (volba > LOBBYSIZE)) {
        receive(hrac->socket, buff);// read the message from client and copy it in buffer
        char *command; // sent command
        char *user; //user name
        command = strtok(buff, ":"); //split received message in name and command
        user = command;
        command = strtok(NULL, ":");
        printf("\n%s sent: %s", user, command);
        // volba mistnosti
        volba = atoi(command);
        if (lobby[volba - 1].pocet_hracu < lobby[volba - 1].max_pocet_hracu) { // hrac se do mistnosti vejde
            hrac->mist = &lobby[volba - 1];
            if (lobby[volba - 1].pocet_hracu == 0) {
                lobby[volba - 1].player1 = *hrac;
            } else {
                lobby[volba - 1].player2 = *hrac;
            }
            lobby[volba - 1].pocet_hracu++;
            memset(sbuff, '\0', size);
            strcpy(sbuff, "Jsi v mistnosti: "); // TODO strncpy
            //strncpy()
            strcat(sbuff, hrac->mist->name);
            strcat(sbuff, "\n");
            send(hrac->socket, sbuff, sizeof(sbuff), 0); // TODO kontrolu v metode
            //printf("%s si vybral %s\n", hrac->name, hrac->mist->name);
            //bzero(sbuff, size);
            //printf("Pokus2.");
        }
        if(hrac->mist->pocet_hracu != 2){
            //printf("Cekame na druheho hrace...");
            strcpy(sbuff, "Cekame na druheho hrace...\n");
            send(hrac->socket, sbuff, sizeof(sbuff), 0);
        }
        //hra(hrac->mist);
    }

}


int main(){
//    struct mistnost lobby[3];

    initializeLobby();

    srand(time(NULL));
    //memset(&lobby, '\0', sizeof(lobby));

    //fork();
    int socketfd, ret;
    struct sockaddr_in serverAddr;

    int newSocket;
    struct sockaddr_in newAddr;

    socklen_t addr_size;

    //char buffer[1024];
    pid_t childpid;

    socketfd = socket(AF_INET,SOCK_STREAM,0);

    if(socketfd < 0){
        printf("\n error in socket creation");
        return -1;
    }
    printf("\n Server socket is created\n");

    memset(&serverAddr, '\0', sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1");

    ret = bind(socketfd, (struct sockaddr*)&serverAddr, sizeof(serverAddr));

    if(ret < 0){
        printf("Error in binding\n");
        return -1;
    }
    printf("[*]Bind to port %d\n", PORT);

    if(listen(socketfd, 10) == 0){
        printf("Listening...\n");
    }else{
        printf("Error in binding\n");
    }

    int size = 256;
    char buff[size];
    char sbuff[size];
    int n;
    int reader;
    memset(buff, 0, size);
    memset(sbuff, 0, size);

    // infinite loop for receiving and sending
    for (;;) {
        newSocket = accept(socketfd, (struct sockaddr*)&newAddr, &addr_size);
        if( newSocket < 0){
            printf("No socket\n");
            exit(1);
        }
        // read the message from client and copy it in buffer
        reader = recv(newSocket, buff, size * sizeof(char), 0);
        if (reader == -1) {
            perror("recv()");
            break;
        } else if (reader == 0) {
            break;
        } else {
            char *command; // sent command
            char *user; //user name
            command = strtok(buff, ":"); //split received message in name and command
            user = command;
            command = strtok(NULL, ":" );
            printf("\n%s sent: %s", user, command);

            if (strncmp(" new", command, 4) == 0) {
                player newPlayer;
                newPlayer.name = (char *)malloc(sizeof(char)*10); // *login must contain up to 10 characters
                strcpy(newPlayer.name, user);
                //newPlayer.name = user;
                newPlayer.gameScore = 0;
                newPlayer.roundScore = 0;
                newPlayer.socket = newSocket;
                //newPlayer.ans = "";
                //newPlayer.mist = null;

                //addPlayerToList(&newPlayer); // pridame hrace do seznamu
                addToLobby(&newPlayer); // pridame hrace do lobby

                printf("Server : %s uspesne prihlasen.\n", newPlayer.name);
                //while(1) { \\ TODO
                    if (newPlayer.mist->pocet_hracu == 2){

                        printMistnost(newPlayer.mist);
                        hra(newPlayer.mist);
                        printf("Konec hry\n");
                        clearMistnost(newPlayer.mist); // mist.pocet_hracu = 0
                        addToLobby(&newPlayer.mist->player1);
                        addToLobby(&newPlayer.mist->player2);
                    }
                //}
            }

            //TODO playing multiple games in different lobby rooms at te same time

            if (strncmp("exit", command, 4) == 0) {
                printf("Server Exit...\n");
                break;
            }
            bzero(buff, size);

            //n = 0;
            printf("To client : OK\n");
            strcpy(sbuff, "OK\n" );

            // copy server message in the buffer
            //while (((sbuff[n++] = getchar()) != '\n'));

            // and send that buffer to client
            write(newSocket, sbuff, sizeof(sbuff));
            //bzero(sbuff, size);
            // }
        }

        if((childpid = fork()) == 0){
            close(socketfd);

//            while(1){
//                printf("MAIN->while(1)\n");
//                read(newSocket, buff, size);
//                printf("MAIN->while(2)\n");
//
//                if(strcmp(buff,":exist") == 0){
//                    printf("Disconnected from %s:%d\n", inet_ntoa(newAddr.sin_addr), ntohs(newAddr.sin_port));
//                    break;
//                } else{
//                    char *command; // sent command
//                    char *user; //user name
//
//                    command = strtok(buff, ":"); //split received message in name and command
//                    user = command;
//                    command = strtok(NULL, ":" );
//                    lobby->player1.ans = command;
//                    printf("Client: %s\n", buff);
//                    //scanf("%s", &sbuff[0]);
//                    //sendMess(newSocket, sbuff, strlen(sbuff));
//                    strcpy(sbuff, "OK\n" );
//                    write(newSocket, sbuff, strlen(sbuff));
//                    bzero(buff, sizeof(buff));
//                    bzero(sbuff, sizeof(sbuff));
//                }
//            }
        }

    }
    close(newSocket);

    return 0;
}