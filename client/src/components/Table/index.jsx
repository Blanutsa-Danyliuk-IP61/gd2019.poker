import React from 'react';
import { connect } from 'react-redux';

import { Grid } from '@material-ui/core';
import Card from '../Card';


import { getTableCards } from '../../util/redux/reducers/main';

const Table = (props) => {

    const { cards } = props;

    return (
          <Grid container justify='space-between'>
              {cards.map(card => (
                  <Grid
                      key={card.short + card.suitEmoji}
                      item
                      container
                      xs={2}
                  >
                    <Card shown card={card} />
                  </Grid>
              ))}
          </Grid>
    );
};

const mapStateToProps = state => ({
    cards: getTableCards(state)
});


export default connect(mapStateToProps)(Table);
