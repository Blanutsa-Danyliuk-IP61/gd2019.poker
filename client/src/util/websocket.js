import Stomp from 'stompjs';
import properties from "../preperties";
import SockJs from 'sockjs-client';


// eslint-disable-next-line no-mixed-operators
const generateUUID = () => 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c, r) => ('x' === c ? (r=Math.random()*16|0):(r&0x3|0x8)).toString(16));

const sock = new SockJs(properties.serverUrl);
const stompClient = Stomp.over(sock);
stompClient.connect({}, function (frame) {
    stompClient.send('/app/connected', {}, generateUUID());

    stompClient.subscribe('/user/queue/notify',  (greeting) => {
        debugger
         console.log("sdfsdfsdfsdf");
    });

});