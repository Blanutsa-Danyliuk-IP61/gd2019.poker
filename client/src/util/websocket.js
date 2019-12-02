import Stomp from 'stompjs';
import properties from "../preperties";

const socket = new WebSocket(properties.serverUrl);
const stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // stompClient.subscribe('/topic/greetings', function (greeting) {
    //     showGreeting(JSON.parse(greeting.body).content);
    // });
});