export const PLAYER_ID_KEY = 'POKER_PLAYER_ID_KEY';

export const savePlayerId = id => localStorage.setItem(PLAYER_ID_KEY, id);
export const getPlayerId = id => localStorage.getItem(PLAYER_ID_KEY);