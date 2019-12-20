import { makeStyles } from '@material-ui/core';

const useStyles = makeStyles({
    btn: {
        width: '100%',
        margin: '5px'
    },
    bit: {
        marginRight: '15px',
        fontWeight: 'bold'
    },
    coins: {
        display: 'inline-box',
        height: '20px',
        width: '20px',
        marginRight: '5px'
    },
    slider: {
        '& .MuiSlider-markLabel': {
            fonWeight: 'bold',
            color: '#ffffff'
        }
    },
    time: {
        fonWeight: 'bold',
        color: '#ffffff'
    }
});

export default useStyles;