import Stomp from 'stompjs';
import properties from "../preperties";
import SockJs from 'sockjs-client';
import { checkUser } from './api';
import storage from '../../src/index';

const sock = new SockJs(properties.websocketUrl);
const stompClient = Stomp.over(sock);

const update = (evenGroup) => {
    if (evenGroup.hasOwnProperty('events')) {
        for (let i = 0; i < evenGroup.events.length; i++) {
            const event = evenGroup.events[i];

            if (evenGroup.delay) {
                setTimeout(() => {
                    storage.dispatch(event);
                }, 1000 * evenGroup.delay * i);
            } else {
                storage.dispatch(event);
            }
        }
    } else {
        storage.dispatch(evenGroup);
    }
};

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

export const raise = (bid) => {
    stompClient.send('/app/raise', [], bid);
};

export const sendChatMessage = (message) => {
    stompClient.send('/app/message', [], JSON.stringify(message));
};

export const connectWS = () => {
    stompClient.connect({}, () => {
        checkUser();

        stompClient.subscribe('/user/queue/update',  (res) => {
            update(JSON.parse(res.body));
        });
    });
};
