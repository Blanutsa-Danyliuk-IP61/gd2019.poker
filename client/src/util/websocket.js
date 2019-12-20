import Stomp from 'stompjs';
import properties from "../preperties";
import SockJs from 'sockjs-client';
import { checkUser } from './api';
import storage from '../../src/index';
import {
    startNewGame,
    startNewRound,
    init,
    newPlayer,
    playerDisconnected,
    callResponse, addChatMessage
} from './redux/reducers/main';

const sock = new SockJs(properties.websocketUrl);
const stompClient = Stomp.over(sock);

export const initConnection = (id, username) => {
    stompClient.send('/app/connect', {}, JSON.stringify({ id, username }));
};

export const call = () => {
    stompClient.send('/app/call');
};

export const check = () => {
    stompClient.send('/app/check');
};

export const fold = () => {
    stompClient.send('/app/fold');
};

export const startFirstRound = () => {
    stompClient.send('/app/startFirstRound');
};

export const sendChatMessage = (message) => {
    stompClient.send('/app/message', [], JSON.stringify(message));
};

export const connectWS = () => {
    stompClient.connect({}, () => {
        checkUser();

        stompClient.subscribe('/user/queue/init',  (res) => {
            storage.dispatch(init(JSON.parse(res.body)));
        });

        stompClient.subscribe('/user/queue/newPlayer',  (res) => {
            storage.dispatch(newPlayer(JSON.parse(res.body)));
        });

        stompClient.subscribe('/user/queue/playerDisconnected',  (res) => {
            storage.dispatch(playerDisconnected(JSON.parse(res.body)));
        });

        stompClient.subscribe('/user/queue/startGame',  (res) => {
            storage.dispatch(startNewGame(JSON.parse(res.body)));
            startFirstRound();
        });

        stompClient.subscribe('/user/queue/startRound',  (res) => {
            storage.dispatch(startNewRound(JSON.parse(res.body)));
        });

        stompClient.subscribe('/user/queue/call',  (res) => {
            storage.dispatch(callResponse(JSON.parse(res.body)));
        });

        stompClient.subscribe('/user/queue/message',  (res) => {
            storage.dispatch(addChatMessage(JSON.parse(res.body)));
        });
    });
};
