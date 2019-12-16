import axios from 'axios';
import properties from '../preperties';
import { getPlayerId } from './localstorage';
import { initConnection } from './websocket';
import store from '../../src/index';
import { newUser } from './redux/reducers/main';

export const checkLogin = (login, callback) => {
    axios.post(`${properties.apiUrl}/check/login`, {data: login})
        .then(res => {
            callback(res.data)
        });
};

export const checkUser = () => {
    axios.post(`${properties.apiUrl}/check/user`, {data: getPlayerId()})
        .then(res => {
            if (res.data) {
                initConnection(getPlayerId())
            } else {
                store.dispatch(newUser());
            }
        });
};

