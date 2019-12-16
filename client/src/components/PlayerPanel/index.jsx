import React, { Fragment } from 'react';
import { connect } from 'react-redux';

import Card from '../Card';
import { Grid, Typography, } from '@material-ui/core';

import useStyles from './styles';
import Coins from '../Coins';
import { getCurrentPlayerId, isGameActive } from '../../util/redux/reducers/main';

const PlayerPanel = (props) => {

    const classes = useStyles();
    const { shown, isGameActive, currentPlayerId } = props;
    const { name, balance, folded, id } = props.playerData;

    let { cards } = props.playerData;

    const current = currentPlayerId === id;

    return (
        <Grid container className={classes.root} style={{backgroundColor: current ? '#598A8B' : 'rgb(0, 61, 0)' }}>
            <Grid item container xs={12}>
                <Grid item container xs={6} justify='center'>
                    <Typography component='h6' className={classes.login}>
                        { name || '---' }
                    </Typography>
                </Grid>
                <Grid item container xs={6} justify='center'>
                    <div>
                        <Coins/>
                        <Typography component='h6' className={classes.balance}>
                            { balance || 0 }
                        </Typography>
                    </div>
                </Grid>
            </Grid>
            <Grid item container xs={12} justify='space-around' className={classes.cardContainer}>
                { isGameActive && name ? ( folded ? <p>Folded!</p> :
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
                        </Fragment>
                    ) : ''
                }
            </Grid>
        </Grid>
    );
};

const mapStateToProps = (state) => ({
    isGameActive: isGameActive(state),
    currentPlayerId: getCurrentPlayerId(state)
});

export default connect(mapStateToProps)(PlayerPanel);
