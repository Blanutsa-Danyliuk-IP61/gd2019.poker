import React from 'react';
import { connect } from 'react-redux';
import { getPlayers, isGameActive, getPrizePool } from '../../util/redux/reducers/main';
import { Grid, Typography } from '@material-ui/core';

import useStyles from './styles';
import Coins from '../Coins';

const BidPanel = (props) => {

    const classes = useStyles();

    return (
        props.isGameActive ?
            <Grid container justify='center' direction='column'>
                <Typography component='div' key='pool' className={classes.boldText}>Price poll - <Coins/> {props.prizePool}</Typography>
                <Typography key='bidsTitle' className={classes.boldText}>Bids</Typography>
                {
                    props.players.map(p => (
                        <Typography component='div' key={p.name}>{`${p.name} -`}<Coins/>{p.bid}</Typography>
                    ))
                }
            </Grid> : ''
    );
};

const mapStateToProps = (state) => ({
    players: getPlayers(state),
    prizePool: getPrizePool(state),
    isGameActive: isGameActive(state)
});

export default connect(mapStateToProps)(BidPanel);