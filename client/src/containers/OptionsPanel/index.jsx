import React, { Fragment, useState } from 'react';
import { connect } from 'react-redux';

import { Grid, Button, Typography, Slider } from '@material-ui/core';

import useStyles from './styles';
import { getCurrentPlayerId, getMaxBid, getPlayer } from '../../util/redux/reducers/main';
import Coins from '../../components/Coins';
import { getPlayerId } from '../../util/localstorage';
import {call, fold, check, raise} from '../../util/websocket';

const OptionPanel = (props) => {

    const classes = useStyles();

    const { balance, isCurrentPlayer, player, maxBid } = props;
    const [ bid, setBid ] = useState(maxBid);

    const hasMoney = player.status !== 'ALL_IN' && player.status !== 'FOLDED' && player.balance > 0;
    const canRaise = isCurrentPlayer && hasMoney && player.balance > maxBid;
    const canCheck = isCurrentPlayer && hasMoney && player.bid === maxBid;
    const canCall = isCurrentPlayer && hasMoney && player.balance >= maxBid && player.bid < maxBid;

    return (
        <Fragment>
            <Grid key='top' container item xs={12}>
                <Grid key='check' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => check()}
                        disabled={!canCheck}
                    >
                        Check
                    </Button>
                </Grid>
                <Grid key='call' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => call()}
                        disabled={!canCall}
                    >
                        Call
                    </Button>
                </Grid>
            </Grid>
            <Grid key='middle' container item xs={12}>
                <Grid key='fold' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => fold()}
                        disabled={!isCurrentPlayer || !hasMoney}
                    >
                        Fold
                    </Button>
                </Grid>
                <Grid key='raise' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => raise(bid)}
                        disabled={!canRaise}
                    >
                        Raise
                    </Button>
                </Grid>
            </Grid>
            <Grid key='bottom' container item xs={8} className={classes.btn}>
                <Typography key='bid' className={classes.bit}>Bit</Typography>
                { isCurrentPlayer && hasMoney ?
                    <Fragment>
                        <Coins key='coins'/>
                        <Typography key='balance' component='h6' className={classes.balance}>
                            { bid === -1 ? 0 : bid }
                        </Typography>
                    </Fragment> : ''
                }
                <Slider
                    key='slider'
                    className={classes.slider}
                    aria-label="custom thumb label"
                    min={maxBid}
                    max={balance}
                    disabled={!isCurrentPlayer || !hasMoney}
                    marks={[
                        {
                            value: maxBid,
                            label: maxBid,
                        },
                        {
                            value: balance,
                            label: balance,
                        },
                    ]}
                    onChange={(event, value) => setBid(value)}
                    color='secondary'
                />
            </Grid>
        </Fragment>
    );
};

const mapStateToProps = (state) => ({
    balance: getPlayer(state).balance,
    isCurrentPlayer: getCurrentPlayerId(state) === getPlayerId(),
    player: getPlayer(state),
    maxBid: getMaxBid(state)
});

export default connect(mapStateToProps)(OptionPanel);
