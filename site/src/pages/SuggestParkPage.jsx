import React, { useEffect, useState } from 'react';
import axios from 'axios';
import logo from '../img/310logo.png';
import {paramsSerializer} from "../components/paramsSerializer";
import InactivityTimer from "../components/InactivityTimer";
import {Link} from "react-router-dom";


const SuggestParkPage = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedEntity, setSelectedEntity] = useState(null);
    const [suggestedPark, setSuggestedPark] = useState(null);
    const [entityNotFound, setEntityNotFound] = useState(false);
    const [loggedInUser, setLoggedInUser] = useState(null);
    const [selectedPark, setSelectedPark] = useState(null);

    const showParkDetails = (park) => {
        setSelectedPark(park);
    };

    useEffect(() => {
        const fetchLoggedInUser = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/checkSession');
                setLoggedInUser(response.data);
                console.log(response.data);
                console.log(typeof response.data);
            } catch (error) {
                console.error('Error fetching logged-in user:', error);
            }
        };

        fetchLoggedInUser();
    }, []);

    const handleSearch = async () => {
        const usernames = searchTerm.split(',').map(name => name.trim()).filter(name => name.length);

        if (usernames.length === 0) {
            setEntityNotFound(true);
            return;
        }

        let foundUsers = [];
        let privateUsers = [];
        let notFoundUsers = [];
        setEntityNotFound(false);

        for (let i = 0; i < usernames.length; i++) {
            try {
                const response = await axios.get(`http://localhost:8080/getUserByUsername`, { params: { username: usernames[i] } });
                if (response.status === 200) {
                    const isPrivateResponse = await axios.get(`http://localhost:8080/isListPrivate`, { params: { username: usernames[i] } });
                    if (isPrivateResponse.data) {
                        privateUsers.push(usernames[i]);
                    } else {
                        foundUsers.push(response.data.username);
                    }
                }
            } catch (error) {
                console.error('Error fetching user details for:', usernames[i], error);
                if (error.response && error.response.status === 404) {
                    notFoundUsers.push(usernames[i]);
                }
            }
        }

        if (foundUsers.length > 0) {
            setSelectedEntity({ members: foundUsers, displayName: foundUsers.join(', ') });
        } else {
            setSelectedEntity(null);
            setEntityNotFound(true);
        }

        if (privateUsers.length > 0) {
            alert(`The following users have private lists and cannot be included in the suggestion: ${privateUsers.join(', ')}`);
        }

        if (notFoundUsers.length > 0) {
            alert(`The following users do not exist: ${notFoundUsers.join(', ')}`);
        }

        setSearchTerm('');
        setSuggestedPark(null);
    };

    const handleSuggest = async () => {
        if (!selectedEntity || !loggedInUser) return;

        const usernames = [loggedInUser.username, ...selectedEntity.members];

        try {
            const response = await axios.post('http://localhost:8080/suggestFavorite', usernames);
            const suggestedParkName = response.data;

            if (suggestedParkName) {
                try {
                    const parkResult = await axios.get("/searchParks", {
                        params: {parkName: suggestedParkName},
                        paramsSerializer
                    });
                    if (parkResult.data.length > 0) {
                        const parkData = parkResult.data[0];
                        setSuggestedPark({
                            parkCode: parkData.parkCode,
                            parkName: parkData.fullName,
                            images: parkData.images.slice(0, 3),
                            description: parkData.description,
                            location: `${parkData.addresses[0]?.city}, ${parkData.addresses[0]?.stateCode}`,
                            url: parkData.url,
                            entranceFees: parkData.entranceFees,
                            topics: parkData.topics,
                            activities: parkData.activities
                        });
                    }
                } catch (error) {
                    console.error('Error fetching park details:', error);
                }
            } else {
                setSuggestedPark(null);
            }
        } catch (error) {
            console.error('Error suggesting favorite park:', error);
        }
    };

    return (
        <div>
            <InactivityTimer timeout={60000} />
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Suggest Park</h1>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'center', gap: '20px' }}>
                    <Link to="/search">
                        <button
                            id="search-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Search
                        </button>
                    </Link>
                    <Link to="/suggest">
                        <button
                            id="suggest-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Suggest
                        </button>
                    </Link>
                    <Link to="/compare">
                        <button
                            id="compare-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Compare
                        </button>
                    </Link>
                    <Link to="/favorites">
                        <button
                            id="favorties-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Favorites
                        </button>
                    </Link>
                    <Link to="/login">
                        <button
                            id="logout-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Log Out
                        </button>
                    </Link>
                </div>
            </div>
            <div style={{ backgroundColor: '#f8f9fa', padding: '15px 0' }}>
                <div className="container">
                    <form onSubmit={(e) => { e.preventDefault(); handleSearch(); }} className="form-inline d-flex justify-content-between align-items-center">
                        <img src={logo} alt="logo" style={{ height: '75px', marginRight: '20px' }} />
                        <input
                            type="text"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="form-control mr-sm-2"
                            placeholder="Search user or group"
                            style={{ margin: '0 5px', flex: '1' }}
                        />
                        <button type="submit" className="btn" style={{
                            padding: '6px 12px',
                            background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                            color: 'white',
                            border: 'none'
                        }} id="search-button-input">Search</button>
                    </form>
                </div>
            </div>
            {entityNotFound && (
                <div style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh',
                    color: 'black',
                    textAlign: 'center',
                    marginTop: '-20vh'
                }}>
                    This user or group does not exist.
                </div>
            )}
            {selectedEntity && (
                <div style={{ display: 'flex', justifyContent: 'center', padding: '20px' }}>
                    <div style={{
                        width: '40%',
                        margin: '20px',
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                        borderRadius: '10px',
                        overflow: 'hidden',
                        textAlign: 'center',
                        background: '#fff',
                    }}>
                        <div style={{ padding: '10px' }}>
                            <h3>{selectedEntity.username || selectedEntity.groupName || selectedEntity.members.join(', ')}</h3>
                            {selectedEntity.members.length > 0 ? (
                                <button onClick={handleSuggest} style={{
                                    padding: '6px 12px',
                                    background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '5px',
                                    cursor: 'pointer',
                                    fontSize: '16px'
                                }} id="suggest-button-input">
                                    Suggest
                                </button>
                            ) : (
                                <p style={{ color: 'red' }}>No users found.</p>
                            )}
                        </div>
                    </div>
                </div>
            )}
            {suggestedPark && (
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    marginTop: '20px'
                }}>
                    <h2>Suggested Park:</h2>
                    <div
                        data-testid={`park-card-${suggestedPark.parkCode}`}
                        id = "suggested-park"
                        style={{

                            width: '60%',
                            margin: '20px',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            borderRadius: '10px',
                            overflow: 'hidden',
                            textAlign: 'center',
                            background: '#fff',
                            cursor: 'pointer' // Add cursor pointer style
                        }}
                        onClick={() => showParkDetails(suggestedPark)} // Add onClick event handler
                    >
                        <div style={{ padding: '10px' }}>
                            {suggestedPark.images.length > 0 && (
                                <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '10px' }}>
                                    {suggestedPark.images.map((image, index) => (
                                        <img
                                            key={index}
                                            src={image.url}
                                            alt={`${suggestedPark.parkName} ${index + 1}`}
                                            style={{
                                                width: '30%',
                                                height: '200px',
                                                objectFit: 'cover',
                                                borderRadius: '10px',
                                                margin: '0 5px'
                                            }}
                                        />
                                    ))}
                                </div>
                            )}
                            <h3>{suggestedPark.parkName}</h3>
                            <p>Location: {suggestedPark.location}</p>
                            <p>{suggestedPark.description}</p>
                        </div>
                    </div>
                </div>
            )}
            {selectedPark && (
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '20px' }}>
                    <div className="specific-result" style={{ width: '60%', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)', borderRadius: '10px', overflow: 'hidden', textAlign: 'center', background: '#fff', padding: '20px' }}>
                        <img src={selectedPark.images[0]?.url} alt={selectedPark.images[0]?.altText || 'Park image'} style={{ width: 'calc(100% - 40px)', margin: '20px', borderRadius: '10px' }} />
                        <h2>{selectedPark.parkName}</h2>
                        <p id="park-description">{selectedPark.description}</p>
                        <p id="park-location">Location: {selectedPark.location}</p>
                        <p id="park-url">URL: <a href={selectedPark.url} target="_blank" rel="noopener noreferrer">{selectedPark.url}</a></p>
                        <p id="entrance-fee">Entrance Fee: {selectedPark.entranceFees[0]?.cost ? `$${selectedPark.entranceFees[0]?.cost}` : 'Free'}</p>
                        <p id="amenities">
                            Amenities:{' '}
                            {selectedPark.topics.map((topic, index) => (
                                <button key={index} style={{ marginRight: '5px' }}>
                                    {topic.name}
                                </button>
                            ))}
                        </p>
                        <p id="activities">
                            Activities:{' '}
                            {selectedPark.activities.map((activity, index) => (
                                <button key={index} style={{ marginRight: '5px' }}>
                                    {activity.name}
                                </button>
                            ))}
                        </p>
                        <button onClick={() => setSelectedPark(null)} id="back-to-suggested-park" style={{ padding: '10px 20px', background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '20px' }}>Back to Suggested Park</button>
                    </div>
                </div>
            )}

        </div>
    );
};

export default SuggestParkPage;