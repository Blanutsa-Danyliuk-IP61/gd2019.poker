import React from 'react';

import { Grid, Typography, Avatar } from '@material-ui/core';
import cardBack from '../../assets/images/cardback.jpg';

import useStyles from './styles';

const Card = (props) => {

    const classes = useStyles();
    const { shown, card } = props;

    const getColorBySuit = (suit) => {
        if (suit === '♥️' || suit === '♦️') {
            return 'red';
        } else {
            return 'black';
        }
    };

    return (
        <Grid container justify='center'>
            {
                shown ? (
                    <Grid container item className={classes.card} direction='column' alignItems='center'>
                        <Grid item xs={6}>
                            <Typography key='short' style={{color: getColorBySuit(card.suitEmoji)}} className={classes.cardFont}>
                                {card.short}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography key='suit' style={{color: getColorBySuit(card.suitEmoji)}} className={classes.cardFont}>
                                {card.suitEmoji}
                            </Typography>
                        </Grid>
                    </Grid>
                ) : (
                    <Avatar className={classes.img} alt='Card back' src={cardBack} variant='square'/>
                )
            }
        </Grid>
    );
};

export default Card;
