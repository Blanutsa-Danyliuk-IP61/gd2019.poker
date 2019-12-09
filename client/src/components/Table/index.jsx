import React from 'react';

import { Grid } from '@material-ui/core';
import Card from '../Card';

import useStyles from './styles';

const Table = (props) => {

    const classes = useStyles();
    const { cards } = props;

    return (
          <Grid container className={classes.root} justify='space-around'>
              {cards.map(card => (
                  <Grid
                      key={card.short + card.suitEmoji}
                      item
                      xs={2}
                  >
                    <Card shown card={card} />
                  </Grid>
              ))}
          </Grid>
    );
};

export default Table;
