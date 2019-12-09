import React, { Fragment } from 'react';

import Card from '../Card';
import { Grid, Typography, Avatar } from '@material-ui/core';
import coins from '../../assets/images/coins.png';

import useStyles from './styles';

const PlayerPanel = (props) => {

    const classes = useStyles();
    const { login, balance, active, folded, cards, shown, current } = props;

    return (
        <Grid container className={classes.root} style={{backgroundColor: current ? '#598A8B' : 'rgb(0, 61, 0)' }}>
            <Grid item container xs={12}>
                <Grid item container xs={6} justify='center'>
                    <Typography component='h6' className={classes.login}>
                        { login }
                    </Typography>
                </Grid>
                <Grid item container xs={6} justify='center'>
                    <div>
                        <Avatar src={coins} className={classes.coins}/>
                        <Typography component='h6' className={classes.balance}>
                            { balance }
                        </Typography>
                    </div>
                </Grid>
            </Grid>
            <Grid item container xs={12} justify='space-around' className={classes.cardContainer}>
                { active ? ( folded ? <p>Folded!</p> :
                        <Fragment>
                            <Grid key='first-player-card' item container xs={5} justify='center'>
                                <Card
                                    shown={shown}
                                    card={cards[0]}
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

export default PlayerPanel;
