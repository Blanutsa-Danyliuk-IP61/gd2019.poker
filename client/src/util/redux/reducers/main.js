import { getPlayerId } from '../../localstorage';
import { initConnection } from '../../websocket';
import store from '../../../index';
import {checkUser} from "../../api";

const defaultPlayerData = {
    cards: [{}, {}]
};

// show message time in seconds
const SHOW_MESSAGE_TIME = 3;
const DELETE_DATA_DELAY = 10;

const initialState = {
    isNewUser: false,
    prizePool: 0,
    tableCards: [],
    infoMessages: [],
    players: [],
    gameStatus: 'WAITING',
    currentPlayerId: null,
    chatMessages: [],
    messageShown: false,
    message: null
};

// actions types
const SET_MESSAGE = 'SET_MESSAGE';
const HIDE_MESSAGE = 'HIDE_MESSAGE';
const NEW_USER = 'NEW_USER';
const NAME_ENTERED = 'REDUX_ACTION_NAME_ENTERED';
const START_GAME = 'START_GAME';
const NEW_ROUND = 'NEW_ROUND';
const INIT = 'INIT';
const DISCONNECTED = 'DISCONNECTED';
const CONNECTED = 'CONNECTED';
const RECONNECTED = 'RECONNECTED';
const CHAT_MESSAGE = 'CHAT_MESSAGE';
const CHAMPION = 'CHAMPION';
const BID_RESULT = 'BID_RESULT';
const NEXT_BID = 'NEXT_BID';
const MATCH_RESULT = 'MATCH_RESULT';
const DELETE_DATA = 'DELETE_DATA';

// actions
export const newUser = () => ({
    type: NEW_USER
});

export const nameEntered = (name) => ({
    type: NAME_ENTERED,
    value: name
});

const hideMessage = () => ({
    type: HIDE_MESSAGE
});

const deleteData = () => ({
    type: DELETE_DATA
});

// selectors
const initialSelector = (state) => state.main;
export const isNewUser = (state) => initialSelector(state).isNewUser;
export const getTableCards = (state) => initialSelector(state).tableCards;
export const getInfoMessages = (state) => initialSelector(state).infoMessages;
export const getChatMessages = (state) => initialSelector(state).chatMessages;

export const getFirstPlayer = (state) => initialSelector(state).players.find(p => p.id !== getPlayerId()) || defaultPlayerData;

export const getSecondPlayer = (state) => {
    const players = initialSelector(state).players;
    let count = 0;

    for (let i = 0; i < players.length; i++) {
        if (players[i].id !== getPlayerId()) {
            if (count === 1) {
                return players[i];
            }

            count ++;
        }
    }

    return  defaultPlayerData;
};

export const getPlayer = (state) => initialSelector(state).players.find(p => p.id === getPlayerId()) || defaultPlayerData;

export const isGameActive = (state) => initialSelector(state).gameStatus === 'ACTIVE';
export const getPlayers = (state) => initialSelector(state).players;
export const getPrizePool = (state) => initialSelector(state).prizePool;
export const getCurrentPlayerId = (state) => initialSelector(state).currentPlayerId;
export const isMessageShown = (state) => initialSelector(state).messageShown;
export const getMessage = (state) => initialSelector(state).message;

export const getMaxBid = (state) => {
    let players = initialSelector(state).players;
    let max = -1;

    for (let i = 0; i < players.length; i++) {
        if (players[i].bid > max) {
            max = players[i].bid;
        }
    }

    return max;
};

export const finished = (state) => initialSelector(state).gameStatus === 'FINISHED';

// reducer
const mainReducer = (state = initialState, action) => {
    switch (action.type) {
        case NAME_ENTERED:
            initConnection(getPlayerId(), action.value);

            return {
                ...state,
                isNewUser: false
            };
        case NEW_USER:
            return {
                ...state,
                isNewUser: true
            };
        case START_GAME: {
            hideMessageWithDelay();

            return {
                ...state,
                gameStatus: 'ACTIVE',
                infoMessages: [...state.infoMessages, 'The game begins!'],
                players: action.players,
                prizePool: 0,
                tableCards: [],
                currentPlayerId: null,
                messageShown: true,
                message:'The game begins!'
            };
        }
        case NEW_ROUND:
            hideMessageWithDelay();

            const infos = state.infoMessages;
            infos.push(`Round ${action.round} begins!`);
            if (action.round === 1) {
                const smallBlind = action.smallBlind;
                const bigBlind = action.bigBlind;

                const smallBlindPlayer = getPlayerById(state, smallBlind.playerId);
                const bigBlindPlayer = getPlayerById(state, bigBlind.playerId);

                infos.push(`${smallBlindPlayer.name} bid ${smallBlind.blind} - (${smallBlind.type.toLowerCase()} blind)!`);
                infos.push(`${bigBlindPlayer.name} bid ${bigBlind.blind} - (${bigBlind.type.toLowerCase()} blind)!`);
            }

            return {
                ...state,
                prizePool: action.prizePool,
                currentPlayerId: action.currentPlayerId,
                players: action.players,
                infoMessages: infos,
                tableCards: action.tableCards,
                messageShown: true,
                message: `Round ${action.round} begins!`
            };
        case INIT:
            return {
                ...state,
                chatMessages: action.tournament.messages,
                prizePool: action.tournament.prizePool,
                players: action.tournament.players,
                gameStatus: action.tournament.status,
                currentPlayerId: action.tournament.currentPlayerId,
                tableCards: action.tournament.tableCards
            };
        case DISCONNECTED: {
            const players = state.players;

            for (let i = 0; i < players.length; i++) {
                if (players[i].id === action.disconnectedPlayer.id) {
                    players[i] = action.disconnectedPlayer;
                }
            }

            return {
                ...state,
                players,
                infoMessages: [...state.infoMessages, `${action.disconnectedPlayer.name} disconnected...`]
            };
        }
        case CONNECTED: {
            const players = state.players;

            let exists = false;
            for (let i = 0; i < players.length; i++) {
                if (players[i].id === action.connectedPlayer.id) {
                    exists = true;
                    players[i] = action.connectedPlayer;
                }
            }

            if (!exists) {
                players.push(action.connectedPlayer);
            }

            return {
                ...state,
                players,
                infoMessages: [...state.infoMessages, `${action.connectedPlayer.name} connected...`]
            };
        }
        case RECONNECTED: {
            const players = state.players;

            for (let i = 0; i < players.length; i++) {
                if (players[i].id === action.reconnectedPlayer.id) {
                    players[i] = action.reconnectedPlayer;
                }
            }

            return {
                ...state,
                players,
                infoMessages: [...state.infoMessages, `${action.reconnectedPlayer.name} reconnected...`]
            };
        }
        case CHAT_MESSAGE:
            return {
                ...state,
                chatMessages: [...state.chatMessages, action.message]
            };
        case SET_MESSAGE:
            return {
                ...state,
                messageShown: true,
                message: action.message
            };
        case HIDE_MESSAGE:
            return {
                ...state,
                messageShown: false,
                message: null
            };
        case CHAMPION: {
            deleteDataWithDelay();

            return {
                ...state,
                currentPlayerId: null,
                gameStatus: 'FINISHED',
                messageShown: true,
                infoMessages: [...state.infoMessages, `${action.championPlayer.name} won!`],
                message: `${action.championPlayer.name} won!`
            };
        }
        case BID_RESULT:
            return {
                ...state,
                prizePool: action.prizePool,
                currentPlayerId: null,
                players: state.players.map(p => p.id === action.player.id ? action.player : p),
                infoMessages: [
                    ...state.infoMessages,
                    `${action.player.name} bid ${action.bid} - (${action.bitType.toLowerCase()})!`
                ]
            };
        case NEXT_BID:
            return {
                ...state,
                currentPlayerId: action.nextPlayerId
            };
        case MATCH_RESULT: {
            deleteDataWithDelay();

            return {
                ...state,
                currentPlayerId: null,
                gameStatus: 'FINISHED',
                messageShown: true,
                message: `${action.champion.name} won! (${action.champion.handType})`
            };
        }
        case DELETE_DATA: {
            return initialState;
        }
        default: return state
    }
};

// utils
const getPlayerById = (state, id) => state.players.find(p => p.id === id);
const hideMessageWithDelay = () => setTimeout(() => store.dispatch(hideMessage()), 1000 * SHOW_MESSAGE_TIME);
const deleteDataWithDelay = () => setTimeout(() => {
    deleteData();
    checkUser();
}, 1000 * DELETE_DATA_DELAY);

export default mainReducer;