import React from 'react';

import { Grid, Typography } from '@material-ui/core';

import useStyles from './styles'

const Card = (props) => {

    const classes = useStyles();
    const { shown, card } = props;

    return (
        <Grid container className={classes.root}>
            {
                shown ? (
                    <Grid
                        justify='center'
                        item
                        container
                        xs={12}
                    >
                        <Typography key='short' className={classes.short}>{card.short}</Typography>
                        <Typography key='suit' className={classes.suitEmoji}>{card.suitEmoji}</Typography>
                    </Grid>
                ) : (
                    <Grid
                        item
                        xs={12}
                        className={classes.backCard}
                    />
                )
            }
        </Grid>
    );
};

export default Card;
