import React from 'react';

import { Grid, Typography, Avatar } from '@material-ui/core';
import cardBack from '../../assets/images/cardback.jpg';

import useStyles from './styles';

const Card = (props) => {

    const classes = useStyles();
    const { shown, card } = props;

    const getColorBySuit = (suit) => {
        if (suit === 'HEARTS' || suit === 'DIAMONDS') {
            return 'red';
        } else {
            return 'black';
        }
    };

    const getEmoji = (suit) => {
        switch (suit) {
            case 'SPADES': return '♠️';
            case 'HEARTS': return '♥️';
            case 'DIAMONDS': return '♦️';
            case 'CLUBS': return '♣️';
            default: return '*';
        }
    };

    return (
        <Grid container justify='center'>
            {
                shown ? (
                    <Grid container item className={classes.card} direction='column' alignItems='center'>
                        <Grid item xs={6}>
                            <Typography key='short' style={{color: getColorBySuit(card.suit)}} className={classes.cardFont}>
                                {card.text}
                            </Typography>
                        </Grid>
                        <Grid item xs={6}>
                            <Typography key='suit' style={{color: getColorBySuit(card.suit)}} className={classes.cardFont}>
                                {getEmoji(card.suit)}
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
