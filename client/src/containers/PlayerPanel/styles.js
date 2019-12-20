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
    balance: {
        display: 'inline-box',
        fontSize: '0.8em',
        fontWeight: 'bold'
    },
    status: {
        display: 'inline-box',
        fontSize: '1em',
        fontWeight: 'bold'
    },
    statusContainer: {
        position: 'absolute',
        top: '33%',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    cardContainer: {
        height: '100%',
        padding: '10px',
        position: 'relative'
    }
});

export default useStyles;