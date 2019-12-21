import React, { Fragment } from 'react';

import Card from '../../components/Card';
import { Grid, Typography, } from '@material-ui/core';

import useStyles from './styles';
import Coins from '../../components/Coins';

const PlayerPanel = (props) => {

    const classes = useStyles();
    const { shown, isGameActive, currentPlayerId } = props;
    const { name, balance, id, status, cards, active } = props.playerData;

    const current = currentPlayerId === id;

    return (
        <Grid key={id + status} container className={classes.root} style={{backgroundColor: current ? '#598A8B' : 'rgb(0, 61, 0)' }}>
            <Grid item container xs={12}>
                <Grid item container xs={6} justify='center'>
                    <Typography component='h6' className={classes.login}>
                        { name || '---' }
                    </Typography>
                </Grid>
                <Grid item container xs={6} justify='center'>
                    <Grid item key='coins'>
                        <Coins/>
                    </Grid>
                    <Grid item key='balance'>
                        <Typography component='h6' className={classes.balance}>
                            { balance || 0 }
                        </Typography>
                    </Grid>
                </Grid>
            </Grid>
            <Grid item container xs={12} justify='space-around' className={classes.cardContainer}
                style={{
                    opacity: status === 'FOLDED' || status === 'DISCONNECTED' ? '0.5' : '1'
                }}
            >
                { isGameActive && name ?
                        <Fragment>
                            <Grid key='first-player-card' item container xs={5} justify='center'>
                                <Card
                                    shown={shown}
                                    card={cards[0] }
                                />
                            </Grid>
                            <Grid key='second-player-card' item container xs={5} justify='center'>
                                <Card
                                    shown={shown}
                                    card={cards[1]}
                                />
                            </Grid>
                        </Fragment> : ''
                }

                {
                    active && status === 'FOLDED' ?
                        <Grid item className={classes.statusContainer}>
                            <Typography component='h6' className={classes.status}>Folded</Typography>
                        </Grid> : ''
                }

                {
                    name && !active ?
                        <Grid item className={classes.statusContainer}>
                            <Typography component='h6' className={classes.status}>Disconnected</Typography>
                        </Grid> : ''
                }

            </Grid> 
        </Grid>
    );
};

export default PlayerPanel;
