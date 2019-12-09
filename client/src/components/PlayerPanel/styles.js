import { makeStyles } from '@material-ui/core';

const useStyles = makeStyles({
    root: {
        margin: '0 auto',
        width: '235px',
        height: '172px',
        border: '3px solid #411f18',
        borderRadius: '5px',
        padding: '3px'
    },
    login: {
        fontSize: '0.9em',
        fontWeight: 'bold',
    },
    coins: {
        display: 'inline-box',
        height: '20px',
        width: '20px',
        marginRight: '5px'
    },
    balance: {
        display: 'inline-box',
        fontSize: '0.8em',
        fontWeight: 'bold'
    },
    cardContainer: {
        padding: '10px'
    }
});

export default useStyles;