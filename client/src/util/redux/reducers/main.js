const initialState = {
    login: null,
    isNewUser: false,
    balance: 1000
};

// actions types
const SET_LOGIN = 'REDUX_ACTION_SET_LOGIN';
const SET_BALANCE = 'REDUX_ACTION_SET_BALANCE';

// actions
export const setLogin = (login) => ({
    type: SET_LOGIN,
    value: login
});

// selectors
const initialSelector = (state) => state.main;
export const getLogin = (state) => initialSelector(state).login;
export const getBalance = (state) => initialSelector(state).balance;
export const isNewUser = (state) => initialSelector(state).isNewUser;

const mainReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_LOGIN:
            return {
                ...state,
                login: action.value,
                isNewUser: false
            };
        case SET_BALANCE:
            return {
                ...state,
                balance: action.value
            };
        default:
            return state
    }
};

export default mainReducer;