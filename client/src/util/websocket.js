import Stomp from 'stompjs';
import properties from "../preperties";
import SockJs from 'sockjs-client';
import uuidv4 from 'uuid/v4';

// eslint-disable-next-line no-mixed-operators
const sock = new SockJs(properties.websocketUrl);
const stompClient = Stomp.over(sock);
stompClient.connect({}, function (frame) {
    stompClient.send('/app/connected', {}, uuidv4());

    stompClient.subscribe('/user/queue/notify',  (greeting) => {
         console.log(JSON.parse(greeting.body));
    });

});