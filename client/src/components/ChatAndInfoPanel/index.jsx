import React from 'react';

import { Grid } from '@material-ui/core';
import InfoPanel from '../InfoPanel';

import useStyles from './styles';

const ChatAndInfoPanel = () => {

    const classes = useStyles();

    return (
        <Grid
            container
            item
            xs={12}
            className={classes.root}
            justify='space-around'
        >
            {/*<Grid*/}
            {/*    key='chat'*/}
            {/*    item xs={5}*/}
            {/*    className={classes.item}*/}
            {/*/>*/}
            <Grid
                key='info-panel'
                item
                xs={5}
                className={classes.item}
            >
                <InfoPanel messages={[]} />
            </Grid>
        </Grid>
    );
};

export default ChatAndInfoPanel;