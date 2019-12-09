const initialState = {
    login: null,
    isNewUser: false
};

// actions types
const SET_LOGIN = 'REDUX_ACTION_SET_LOGIN';

// actions
export const setLogin = (login) => ({
    type: SET_LOGIN,
    value: login
});

// selectors
const initialSelector = (state) => state.main;
export const getLogin = (state) => initialSelector(state).login;
export const isNewUser = (state) => initialSelector(state).isNewUser;

const mainReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_LOGIN:
            return {
                ...state,
                login: action.value,
                isNewUser: false
            };
        default:
            return state
    }
};

export default mainReducer;