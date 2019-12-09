import { makeStyles } from '@material-ui/core';
import cardBack from '../../assets/images/cardback.jpg';

const useStyles = makeStyles({
    card: {
        backgroundColor: '#ffffff',
        borderRadius: '5px',
        width: '85px',
        height: '120px',
        fontSize: '1.2em !important'
    },
    cardFont: {
        fontSize: '1.2em !important'
    },
    img: {
        width: '85px',
        height: '120px'
    }
});

export default useStyles;
