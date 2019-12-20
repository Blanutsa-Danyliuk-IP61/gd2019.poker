import React, { Fragment, useState } from 'react';
import { connect } from 'react-redux';

import { Grid, Button, Typography, Slider } from '@material-ui/core';
import Timer from '../Timer';

import useStyles from './styles';
import { getCurrentPlayerId, getPlayer } from '../../util/redux/reducers/main';
import Coins from '../../components/Coins';
import { getPlayerId } from '../../util/localstorage';
import { call, fold, check } from '../../util/websocket';

const minBit = 5;

const OptionPanel = (props) => {

    const classes = useStyles();
    const { callbacks } = props;

    const { balance, isCurrentPlayer } = props;
    const [ bit, setBit ] = useState(minBit);

    return (
        <Fragment>
            <Grid key='top' container item xs={12}>
                <Grid key='check' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => check()}
                        disabled={!isCurrentPlayer}
                    >
                        Check
                    </Button>
                </Grid>
                <Grid key='call' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => call()}
                        disabled={!isCurrentPlayer}
                    >
                        Call
                    </Button>
                </Grid>
                <Grid key='time' container item xs={4} justify='center'>
                    <Timer />
                </Grid>
            </Grid>
            <Grid key='middle' container item xs={12}>
                <Grid key='fold' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => fold()}
                        disabled={!isCurrentPlayer}
                    >
                        Fold
                    </Button>
                </Grid>
                <Grid key='raise' container item xs={4}>
                    <Button
                        variant='contained'
                        className={classes.btn}
                        onClick={() => callbacks.Deal('player')}
                        disabled={!isCurrentPlayer}
                    >
                        Raise
                    </Button>
                </Grid>
            </Grid>
            <Grid key='bottom' container item xs={8} className={classes.btn}>
                <Typography key='bid' className={classes.bit}>Bit</Typography>
                <Coins key='coins'/>
                <Typography key='balance' component='h6' className={classes.balance}>
                    { bit }
                </Typography>
                <Slider
                    key='slider'
                    className={classes.slider}
                    aria-label="custom thumb label"
                    min={minBit}
                    max={balance}
                    disabled={!isCurrentPlayer}
                    marks={[
                        {
                            value: minBit,
                            label: minBit,
                        },
                        {
                            value: balance,
                            label: balance,
                        },
                    ]}
                    onChange={(event, value) => setBit(value)}
                    color='secondary'
                />
            </Grid>
        </Fragment>
    );
};

const mapStateToProps = (state) => ({
    balance: getPlayer(state).balance,
    isCurrentPlayer: getCurrentPlayerId(state) === getPlayerId()
});

export default connect(mapStateToProps)(OptionPanel);
