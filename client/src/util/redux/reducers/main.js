import { getPlayerId } from '../../localstorage';
import { initConnection } from '../../websocket';

const defaultPlayerData = {
    cards: [{}, {}]
};

const initialState = {
    login: null,
    isNewUser: false,
    prizePool: 0,
    tableCards: [],
    infoMessages: [],
    players: [],
    isGameActive: false,
    currentPlayerId: null,
    chatMessages: []
};

// actions types
const SET_LOGIN = 'REDUX_ACTION_SET_LOGIN';
const NEW_USER = 'REDUX_ACTION_NEW_USER';
const ADD_TABLE_CARDS = 'ADD_TABLE_CARDS';
const ADD_INFO_MESSAGE = 'REDUX_ACTION_ADD_INFO_MESSAGE';
const ADD_PLAYER = 'REDUX_ACTION_ADD_PLAYER';
const REMOVE_PLAYER = 'REDUX_ACTION_REMOVE_PLAYER';
const SET_PLAYERS = 'REDUX_ACTION_SET_PLAYERS';
const SET_IS_GAME_ACTIVE = 'REDUX_ACTION_SET_IS_GAME_ACTIVE';
const SET_PRIZE_POOL = 'REDUX_ACTION_SET_PRIZE_POOL';
const START_NEW_GAME = 'REDUX_ACTION_START_NEW_GAME';
const START_NEW_ROUND = 'REDUX_ACTION_START_NEW_ROUND';
const INIT = 'REDUX_ACTION_INIT';
const NEW_PLAYER = 'REDUX_ACTION_NEW_PLAYER';
const PLAYER_DISCONNECTED = 'REDUX_ACTION_PLAYER_DISCONNECTED';
const CALL = 'REDUX_ACTION_CALL';
const NEW_CHAT_MESSAGE = 'REDUX_ACTION_NEW_CHAT_MESSAGE';

// actions
export const setLogin = (login) => ({
    type: SET_LOGIN,
    value: login
});

export const newUser = () => ({
    type: NEW_USER
});

export const addTableCard = (cards) => ({
    type: ADD_TABLE_CARDS,
    value: cards
});

export const addInfoMessage = (messages) => ({
    type: ADD_INFO_MESSAGE,
    value: messages
});

export const addPlayer = (player) => ({
    type: ADD_PLAYER,
    value: player
});

export const removePlayer = (playerId) => ({
    type: REMOVE_PLAYER,
    value: playerId
});

export const setPlayers = (players) => ({
    type: SET_PLAYERS,
    value: players
});

export const setIsGameActive = (value) => ({
    type: SET_IS_GAME_ACTIVE,
    value
});

export const setPrizePool = (pool) => ({
    type: SET_PRIZE_POOL,
    value: pool
});

export const startNewGame = (data) => ({
    type: START_NEW_GAME,
    value: data
});

export const startNewRound = (data) => ({
    type: START_NEW_ROUND,
    value: data
});

export const init = (data) => ({
    type: INIT,
    value: data
});

export const newPlayer = (data) => ({
    type: NEW_PLAYER,
    value: data
});

export const playerDisconnected = (data) => ({
    type: PLAYER_DISCONNECTED,
    value: data
});

export const callResponse = (data) => ({
    type: CALL,
    value: data
});

export const addChatMessage = (data) => ({
    type: NEW_CHAT_MESSAGE,
    value: data
});

// selectors
const initialSelector = (state) => state.main;
export const getLogin = (state) => initialSelector(state).login;
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

export const isGameActive = (state) => initialSelector(state).isGameActive;
export const getPlayers = (state) => initialSelector(state).players;
export const getPrizePool = (state) => initialSelector(state).prizePool;
export const getCurrentPlayerId = (state) => initialSelector(state).currentPlayerId;

const getPlayerById = (state, id) => state.players.find(p => p.id === id);
const shuffle = (array) => {
    if (array[0] && array[1]) {
        const tmp = array[0];
        array[0] = array[1];
        array[1] = tmp;
    }

    return array;
};

const mainReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_LOGIN:
            initConnection(getPlayerId(), action.value);

            return {
                ...state,
                login: action.value,
                isNewUser: false
            };
        case SET_PRIZE_POOL:
            return {
                ...state,
                prizePool: action.value
            };
        case NEW_USER:
            return {
                ...state,
                isNewUser: true
            };
        case ADD_TABLE_CARDS:
            return {
                ...state,
                tableCards: [...state.tableCards, ...action.value]
            };
        case ADD_INFO_MESSAGE:
            return {
                ...state,
                infoMessages: [...state.infoMessages, action.value]
            };
        case ADD_PLAYER:
            return {
                ...state,
                players: [...state.players, action.value]
            };
        case REMOVE_PLAYER:
            return {
                ...state,
                players: state.players.filter(p => p.id !== action.value)
            };
        case SET_PLAYERS:
            return {
                ...state,
                players: action.value
            };
        case SET_IS_GAME_ACTIVE:
            return {
                ...state,
                isGameActive: action.value
            };
        case START_NEW_GAME:
            const playerId = getPlayerId();

            return {
                ...state,
                isGameActive: true,
                infoMessages: [...state.infoMessages, 'The game begins!'],
                players: state.players.map(p => {
                    p.balance = action.value.defaultBalance;

                    if (p.id === playerId) {
                        p.cards = action.value.cards;
                    } else {
                        p.cards = [{}, {}];
                    }

                    return p;
                })
            };
        case START_NEW_ROUND:
            const smallBlind = action.value.smallBlind;
            const bigBlind = action.value.bigBlind;

            const smallBlindPlayer = getPlayerById(state, smallBlind.playerId);
            const bigBlindPlayer = getPlayerById(state, bigBlind.playerId);

            return {
                ...state,
                prizePool: action.value.prizePool,
                currentPlayerId: action.value.currentPlayerId,
                players: state.players.map(p => {
                    if (p.id === smallBlind.playerId) {
                        p.bid = smallBlind.blind;
                        p.balance = smallBlind.balance;
                    }

                    if (p.id === bigBlind.playerId) {
                        p.bid = bigBlind.blind;
                        p.balance = bigBlind.balance;
                    }

                    return p;
                }),
                infoMessages: [
                    ...state.infoMessages,
                    `Round ${action.value.roundIndex} begins!`,
                    `${smallBlindPlayer.name} bid ${smallBlind.blind} - (${smallBlind.type.toLowerCase()} blind)!`,
                    `${bigBlindPlayer.name} bid ${bigBlind.blind} - (${bigBlind.type.toLowerCase()} blind)!`
                ]
            };
        case INIT:
            return {
                ...state,
                chatMessages: action.value.messages,
                prizePool: action.value.prizePool,
                players: action.value.players,
                isGameActive: action.value.status === 'ACTIVE'
            };
        case NEW_PLAYER:
            return {
                ...state,
                players: [...state.players, action.value],
                infoMessages: [...state.infoMessages,`${action.value.name} connected...` ]
            };
        case PLAYER_DISCONNECTED: {
            return {
                ...state,
                players: shuffle(state.players.map(p => {
                    if (p.id === action.value.playerId) {
                        p.status = 'DISCONNECTED';
                    }

                    return p;
                })),
                infoMessages: [...state.infoMessages, `${getPlayerById(state, action.value.playerId).name} disconnected...`]
            };
        }
        case CALL:
            return {
                ...state,
                prizePool: action.value.prizePool,
                currentPlayerId: action.value.currentPlayerId,
                players: state.players.map(p => {
                    if (p.id === action.value.callPlayerId) {
                        p.balance = action.value.callPlayerBalance;
                        p.bid = action.value.callPlayerBet;
                    }

                    return p;
                }),
                infoMessages: [
                    ...state.infoMessages,
                    `${getPlayerById(state, action.value.callPlayerId).name} called ${action.value.callPlayerBet}${action.value.allIn ? ' (all-in)' : ''}!`,
                ]
            };
        case NEW_CHAT_MESSAGE:
            return {
                ...state,
                chatMessages: [...state.chatMessages, action.value]
            };
        default: return state
    }
};

export default mainReducer;