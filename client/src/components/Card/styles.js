import { makeStyles } from '@material-ui/core';
import cardBack from '../../assets/images/cardback.jpg';

const useStyles = makeStyles({
    root: {
        height: '86px',
    },
    backCard: {
        backgroundImage: `url(${cardBack})`,
        backgroundSize: '100%',
        backgroundRepeat: 'no-repeat'
    }
});

export default useStyles;
