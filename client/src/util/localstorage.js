import uuidv4 from 'uuid/v4';

export const PLAYER_ID_KEY = 'POKER_PLAYER_ID_KEY';

export const getPlayerId = () => {
    let playerId = localStorage.getItem(PLAYER_ID_KEY);

    if (!playerId) {
        playerId = uuidv4();
        localStorage.setItem(PLAYER_ID_KEY, playerId);
    }

    return playerId;
};

export const deletePlayerId = () => {
    localStorage.removeItem(PLAYER_ID_KEY);
};