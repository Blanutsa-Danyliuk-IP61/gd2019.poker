import React from 'react';

import { Grid } from '@material-ui/core';
import PlayerHand from '../PlayerHand';
import OptionPanel from '../OptionsPanel';

import useStyles from './styles';

const PlayerDashboard = (props) => {

    const classes = useStyles();
    const { data, options, callbacks } = props;

    return (
        <Grid className={classes.root}>
            <div id='status-wrapper'>
                <h4>{`Player${data.active ? '' : ' (folded)'}`}</h4>
            </div>
            <PlayerHand hand={data.hand} />
            <OptionPanel options={options} callbacks={callbacks} />
        </Grid>
    );
};

export default PlayerDashboard;
