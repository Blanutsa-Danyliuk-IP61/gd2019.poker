import React  from 'react';
import { connect } from 'react-redux';
import { getInfoMessages } from '../../util/redux/reducers/main';

import './styles.css';

const InfoPanel = (props) => {

    return (
        <div className='info-panel'>
            {
                props.messages.map((str) => (
                    <p
                        className='info'
                        key={str.substring(0, 3) + str.substring(str.length - 3, str.length) + str.length}
                    >
                        {str}
                    </p>
                ))
            }
        </div>
    );
};

const mapStateToProps = state => {
    return {
    messages: getInfoMessages(state)
}};

export default connect(mapStateToProps)(InfoPanel);
