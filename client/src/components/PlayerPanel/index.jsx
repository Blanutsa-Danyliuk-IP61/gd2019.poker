import React, { Fragment } from 'react';

import Card from '../Card';
import { Grid, Typography } from '@material-ui/core';

import useStyles from './styles';

const PlayerPanel = ({ login, active, folded }) => {

    const classes = useStyles();

    return (
        <Grid container item xs={12} className={classes.root} justify='center'>
            <Typography
                component='h6'
                className={classes.login}
            >
                { login }
            </Typography>
            <Grid
                item
                container
                xs={12}
                justify='space-between'
                className={classes.cardContainer}
            >
                { active ? ( folded ? <p>Folded!</p> :
                        <Fragment>
                            <Grid
                                key='first-player-card'
                                item
                                xs={5}
                            >
                                <Card
                                    shown={false}
                                />
                            </Grid>
                            <Grid
                                key='second-player-card'
                                item
                                xs={5}
                            >
                                <Card
                                    shown={false}
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
