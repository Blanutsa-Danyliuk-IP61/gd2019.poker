import React  from 'react';
import { connect } from 'react-redux';
import {getChatMessages, getPlayer} from '../../util/redux/reducers/main';

import './styles.css';
import {sendChatMessage} from "../../util/websocket";

const Chat = (props) => {

    let { messages, player } = props;

    const sendMessage = () => {
        const input = document.querySelector('.chat-container .msg');
        const text = input.value;

        if (text.length > 0) {
            sendChatMessage({
                name: player.name,
                text
            });

            document.querySelector('.chat-container .msg').value = '';
            const messageContainer = document.querySelector('.chat-container .messages');
            messageContainer.scrollTop = messageContainer.scrollHeight
        }
    };

    const onKeyDown = (event) => {
        if (event.keyCode === 13) {
            sendMessage();
        }
    };

    return (
        <div className='chat-container'>
            <ul className='messages'>
                {messages.map((msg, k) => {
                    return <li key={k}><span className='msgSender'>{msg.name}:</span> {msg.text}</li>
                })}
            </ul>
            <div className='form'>
                <input autoComplete="off" className='msg' placeholder='Message' maxLength={30} onKeyDown={onKeyDown}/>
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
};

const mapStateToProps = state => ({
    player: getPlayer(state),
    messages: getChatMessages(state)
});

export default connect(mapStateToProps)(Chat);
